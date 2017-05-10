default: build dockerize

build:
	mvn clean package -U -Dmaven.test.skip=true

dockerize:
	docker build -f linkingbenchmarkcontroller.docker -t git.project-hobbit.eu:4567/jsaveta1/linkingbenchmarkcontroller .
	docker build -f linkingdatagenerator.docker -t git.project-hobbit.eu:4567/jsaveta1/linkingdatagenerator .
	docker build -f linkingtaskgenerator.docker -t git.project-hobbit.eu:4567/jsaveta1/linkingtaskgenerator .
	docker build -f linkingevaluationmodule.docker -t git.project-hobbit.eu:4567/jsaveta1/linkingevaluationmodule .
	docker build -f linkingsystemadapter.docker -t git.project-hobbit.eu:4567/jsaveta1/linkingsystemadapter .

	docker push git.project-hobbit.eu:4567/jsaveta1/linkingbenchmarkcontroller
	docker push git.project-hobbit.eu:4567/jsaveta1/linkingdatagenerator
	docker push git.project-hobbit.eu:4567/jsaveta1/linkingtaskgenerator
	docker push git.project-hobbit.eu:4567/jsaveta1/linkingevaluationmodule
	docker push git.project-hobbit.eu:4567/jsaveta1/linkingsystemadapter
	
	