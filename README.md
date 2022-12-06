# InsideWorld engine

## Architecture

This project divide on several sections:

[Core](./core/README.md) - Core functionality of the engine.

[Framework](./frameworks/README.md) - Addition implementation to support different frameworks.

[Plugins](./plugins/README.md) - Maven plugins module.

[Utils](./utils/README.md) - Different usefull utiles.

Please read according README.md file. 

## How to use


## Info

Update version of maven - mvn versions:set -DnewVersion="0.7.0"



#What need to do:

## To enable action engine


## AMQP

Есть авторизация по AMQP по токену.

Для этого необходимо чтобы была добавлена зависиомть на security-amqp-engine и 


# What's need to redefine

## Common security engine

Need to redefine AbstractLoginAction, AbstractReadRolesAction and AbstractReadUserAction


Транзакции автоматически комитяться!