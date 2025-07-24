## Guia de Uso: Git Flow - apiconsultacreditos

Este documento descreve o fluxo de trabalho com Git Flow utilizado no projeto apiconsultacreditos.

# Objetivo

Organizar o ciclo de vida de desenvolvimento com branches bem definidas:

    main: produção

    develop: linha principal de desenvolvimento

    feature/*: novas funcionalidades

    release/*: preparação para release

    hotfix/*: correções emergenciais na produção

## Setup Inicial

# Instale Git Flow (Linux/Mac)
    sudo apt install git-flow

# Ou com Homebrew (Mac)
    brew install git-flow-avh

# Inicialize o git flow
     cd apiconsultacreditos
    git flow init

Responda:

Branch de produção: main
Branch de desenvolvimento: develop
Prefixo de feature: feature/
Prefixo de release: release/
Prefixo de hotfix: hotfix/
Prefixo de suporte: support/
Tag prefixo: (vazio)

 Criando uma nova funcionalidade

    git flow feature start nome-da-feature

# ... desenvolva normalmente

    git flow feature finish nome-da-feature

Esse comando faz merge automático para develop e deleta a branch feature.

# Subindo o código

    git push origin develop

A branch develop é onde todo o código é integrado e testado.

# Criando um Release

     git flow release start 1.0.0

# opcional: ajustes finais

    git flow release finish 1.0.0

* Esse comando:

- Faz merge em main e develop

- Cria uma tag 1.0.0

    git push origin main develop --tags

## Corrigindo um bug em produção

    git flow hotfix start bug-descricao
    
# ... corrige

    git flow hotfix finish bug-descricao

    git push origin main develop --tags

### Regras

1)- Nunca desenvolva na main

2)- Nunca faça merge manual develop → main

3)- Use release para deploys e hotfix para produção

### CI/CD

Toda alteração na branch main ou develop dispara testes via GitHub Actions.

# Ver .github/workflows/ci.yml

## Referência

https://danielkummer.github.io/git-flow-cheatsheet/

https://nvie.com/posts/a-successful-git-branching-model/

[Voltar](README.md)
