#!/usr/bin/env bash

set -x
max="$1"
url="$2"
bucket="$3"
date
echo "url: $url
rate: $max calls / second
bucket: $bucket"
echo "bucket:"
START=$(date +%s);

get () {
  curl -d "{\"bucket\":\"$2\"}" \
      -s -v "$1" 2>&1 | tr '\r\n' '\\n' | awk -v date="$(date +'%r')" '{print $0"\n-----", date}' >> /tmp/perf-test.log
}

while true
do
  echo $(($(date +%s) - START)) | awk '{print int($1/60)":"int($1%60)}'
  sleep 1

  for i in `seq 1 $max`
  do
    get $url $bucket &
  done
done
