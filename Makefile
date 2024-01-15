APP := $(shell basename $(shell git remote get-url origin))
REGISTRY := devdp
VERSION=$(shell git describe --tags --abbrev=0)-$(shell git rev-parse --short HEAD)
#TARGETOS linux darwin windows
TARGETOS=linux
#TARGETARCH amd64 arm64
TARGETARCH=amd64

format:
		gofmt -s -w ./

lint:
		golint

test:

		go test -v

get:
		go get

build: format get
		CGO_ENABLED=0 GOOS=${TARGETOS} GOARCH=${TARGETARCH} go build -v -o kbot -ldflags "-X="github.com/dm-ol/kbot/cmd.appVersion=${VERSION}

image:
		docker build . -t ${REGISTRY}/${APP}:${VERSION}-${TARGETOS}-${TARGETARCH}
		# docker build . -t ghcr.io/dm-ol/kbot:${VERSION}-${TARGETOS}-${TARGETARCH}

push:
		docker push ${REGISTRY}/${APP}:${VERSION}-${TARGETOS}-${TARGETARCH}
		# docker push ghcr.io/dm-ol/kbot:${VERSION}-${TARGETOS}-${TARGETARCH}

clean:
		rm -rf kbot
