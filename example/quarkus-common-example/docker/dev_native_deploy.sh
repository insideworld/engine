#!/bin/sh
POST_TAG=2.0.0

docker buildx build \
-t insideworld.asuscomm.com/engine/backend/$POST_TAG:build \
-f build.dockerfile ../../../ \
--build-arg POST_TAG="$POST_TAG" \
&& \
docker run \
--rm \
--name engine-example-quarkus-build \
-v m2:/home/maven/m2 \
-v ${PWD}/../../../build:/usr/src/engine/example/quarkus-common-example/target \
insideworld.asuscomm.com/engine/backend/$POST_TAG:build \
mvn install -s /usr/share/maven/conf/settings_private.xml -Pnative -Dmaven.test.skip=true \
&& \
docker buildx build \
-t insideworld.asuscomm.com/engine/backend/$POST_TAG:native \
-f image-native.dockerfile ../../../ \
--build-arg POST_TAG="$POST_TAG" \
--cache-from insideworld.asuscomm.com/engine/backend/$POST_TAG:native \
--push