# gerproc

O gerproc é o sistema de gerenciamento de processos solicitado e está finalizado.

O SGBD escolhido para o projeto foi o H2. Ele foi embutido no war da aplicação e 
o banco de dados foi configurado para funcionar em memória. Isto é, após reiniciar 
a aplicação, os dados salvos são perdidos. Esse modo de utilização do banco 
de dados (em memória) foi escolhido para, ao executar como container docker, 
não necessitar criar volumes.

Foi criado um script que insere um usuário com permissão de administrador, para 
que se possa registrar mais usuários com algum perfil e executar as demais 
funcionalidades do sistema.

O usuário ADMIN padrão registrado no sistema é o seguinte:
	
username = admin
password = admin

O servidor tomcat embutido está executando na porta 8080.

# como executar

Para executar a aplicação, basta executar o arquivo "gerproc-0.0.1-SNAPSHOT.war" com o seguinte comando:

java -jar gerproc-0.0.1-SNAPSHOT.war

A url de acesso a tela de login do sistema é: http://localhost:8080/

Após executada a aplicação, se pode criar usuários com diferentes perfis e fazer login 
com eles para acessar as funções da aplicação conforme o perfil de usuário logado.

# executando no docker

Para executar no docker, basta construir a imagem navegando para pasta raiz da aplicação 
(onde está o arquivo Dockerfile) e utilizando o seguinte comando:

docker build -t genproc:v1 .

Após construida a imagem, basta criar e rodar o container com base na imagem criada, executando 
o seguinte comando:

docker run --name genproc -p 8080:8080 genproc:v1

E, então, acessar a url da aplicação: http://localhost:8080/
