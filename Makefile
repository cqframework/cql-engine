build:
	docker build . -t dbcg/intellij-community

run:	
	docker run -it \
  	-v /tmp/.X11-unix:/tmp/.X11-unix \
  	-e DISPLAY=unix${DISPLAY} \
  	--volume ${HOME}:/home/intellij \
  	-p 8080:8080 \
  	dbcg/intellij-community
