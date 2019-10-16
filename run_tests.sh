#!/bin/bash

IMAGE="jk$1"

docker run --rm -it \
    -v $PWD/tests/functional_tests:/functional_tests:ro \
    -v /var/run/docker.sock:/var/run/docker.sock \
    gcr.io/cloud-marketplace-ops-test/functional_test \
    --verbose \
    --vars IMAGE=$IMAGE \
    --vars UNIQUE=$RANDOM \
    --test_spec /functional_tests/auth_test.yaml


docker run --rm -it \
    -v $PWD/tests/functional_tests:/functional_tests:ro \
    -v /var/run/docker.sock:/var/run/docker.sock \
    gcr.io/cloud-marketplace-ops-test/functional_test \
    --verbose \
    --vars IMAGE=$IMAGE \
    --vars UNIQUE=$RANDOM \
    --test_spec /functional_tests/monitoring_test.yaml


docker run --rm -it \
    -v $PWD/tests/functional_tests:/functional_tests:ro \
    -v /var/run/docker.sock:/var/run/docker.sock \
    gcr.io/cloud-marketplace-ops-test/functional_test \
    --verbose \
    --vars IMAGE=$IMAGE \
    --vars UNIQUE=$RANDOM \
    --test_spec /functional_tests/prometheus_test.yaml
