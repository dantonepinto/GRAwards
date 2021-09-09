# GRAwards

GRAwards é uma aplicação Java responsável por manter todos os filmes do Golden Raspberry Awards.


A aplicação utiliza H2 Database em memória, hibernate para persistência e foi toda anotada para ser executada em um container EJB. No entanto, para fins didáticos, utilizei um servidor HTTP simples e emulei o funcionamento do EJB de forma bem rudimentar.

Criei alguns índices para otimização das consultas que retornam o maior e o menor intervalo entre prêmios consecutivos.

Os nomes de produtores e de estúdios contidos nas células, foram separados em cada vírgula e cada expressão " and ". Não está sendo tratado qualquer outro padrão de separação de nomes.

## Instalação

Utilize o [maven](https://dlcdn.apache.org/maven/maven-3/3.8.2/binaries/apache-maven-3.8.2-bin.zip) para realizar a instalação do aplicativo:

```bash
mvn clean install
```

## Teste
Para realizar os testes, execute:
```bash
mvn test
```

## Inicialização

Após a instalação, execute o comando a seguir para iniciar a aplicação.

```bash
mvn exec:java -Dexec.args="8080"
```
A porta padrão é a 8080.
Ao iniciar a aplicação, todas as URLs serão exibidas no console e a url [http://localhost:8080/producers/findByMinMaxIntervalBetweenAwards](http://localhost:8080/producers/findByMinMaxIntervalBetweenAwards) será aberta automaticamento no navegador padrão.
