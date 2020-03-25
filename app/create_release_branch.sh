#!/bin/bash

set -u
set -e

# Includes utility functions
DIR="${BASH_SOURCE%/*}"
if [[ ! -d "$DIR" ]]; then DIR="$PWD"; fi
# shellcheck source=/utils.sh
. "$DIR/utils.sh"

getReleaseBranchName() {
  local versionName
  local releaseVersion
  if [ $# = 1 ] && [ -n "$1" ]; then
    versionName="$1"
    IFS='.' read -r -a majMinPatch <<<"$versionName"
    releaseVersion="${majMinPatch[0]}.${majMinPatch[1]}"
    echo "release/$releaseVersion"
  else
    exit 1 # Missing argument
  fi
}

checkIfBranchExists() {
  local releaseBranchName
  local branchSha
  if [ $# = 1 ] && [ -n "$1" ]; then
    git fetch --all
    releaseBranchName="$1"
    branchSha=$(git rev-parse --verify --quiet "$releaseBranchName")
    if [ -n "$branchSha" ]; then
      return 0 # Branch already exists
    else
      return 1 # Branch does not exist
    fi
  else
    exit 1 # Missing argument
  fi
}

createReleaseBranchLocally() {
  local releaseBranchName
  if [ $# = 1 ] && [ -n "$1" ]; then
    releaseBranchName="$1"
    if ! checkIfBranchExists "$releaseBranchName"; then
      git checkout -b "$releaseBranchName"
      return 0 # Branch created locally
    else
      return 1 # Branch already existed. No need to create.
    fi
  else
    exit 1 # Missing argument
  fi
}

createInternalReleaseTag() {
  local versionName
  if [ $# = 1 ] && [ -n "$1" ]; then
    versionName="$1"
    git fetch --tags
    tagName="ReleaseInternal_$versionName"
    if ! git tag -a "$tagName" -m "$tagName"; then
      return 1 # Error creating tag
    else
      return 0 # Tag created
    fi
  else
    exit 1 # Missing argument
  fi
}

rollback() {
  local tagName
  local releaseBranchName
  local deleteBranchOnRollback
  if [ $# = 3 ] && [ -n "$1" ] && [ -n "$2" ]; then
    tagName="$1"
    releaseBranchName="$2"
    deleteBranchOnRollback="$3"
    git tag -d "${tagName}"
    if [ "$deleteBranchOnRollback" = true ]; then
      git checkout master
      git branch -d "$releaseBranchName";
    fi
  else
    exit 1 # Invalid arguments
  fi
}

# Main flow
versionName=$(getVersionName)
releaseBranchName=$(getReleaseBranchName "$versionName")

echo "Checkout release branch"
if createReleaseBranchLocally "$releaseBranchName"; then
  deleteBranchOnRollback=true
else
  deleteBranchOnRollback=false
fi

echo "Create internal release tag"
if ! createInternalReleaseTag "$versionName"; then
  echo "Error while creating tag $tagName."
  exit 0
fi

echo "Push internal release tag"
if ! git push -u origin "$tagName"; then
  echo "Failed to push tag: $tagName. Rolling back."
  rollback "$tagName" "$releaseBranchName" $deleteBranchOnRollback
  exit 0
fi

echo "Push release branch"
git push -u origin "$releaseBranchName"

echo "Created release branch $releaseBranchName and tag $tagName"
