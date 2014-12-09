wordrails-webclient2
====================

Novo cliente web wordrails

Para executar o aplicativo localmente edite o arquivo /js/app.js e procure pela constante ```WORDRAILS```.

Em seguida, altere a propriedade baseUrl para apontar para o servidor que desejar:
```
...
.constant('WORDRAILS', {
    baseUrl: "http://localhost:8080",
    pageSize: 10
})
```

Em seguida, procure a linha ```authService.signIn``` e passe como parâmetro o usuário de testes que desejar:
```
...
.run(function($rootScope, $location, authService){

    authService.signIn("wordrails", "wordrails");

```
