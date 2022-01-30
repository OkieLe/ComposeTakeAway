#!/usr/bin/env sh

for entry in ./hooks/*; do
    name="$(basename -- $entry)"
    echo "Setting up $name"
    cp "$entry" "../.git/hooks/$name"
    chmod a+x "../.git/hooks/$name"
    echo "$name set up"
done

