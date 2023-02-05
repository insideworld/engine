# InsideWorld engine

## Architecture

This project divide on several sections:

[Core](./core/README.md) - Core functionality of the engine.

[Framework](./frameworks/README.md) - Addition implementation to support different frameworks.

[Plugins](./plugins/README.md) - Maven plugins module.

[Utils](./utils/README.md) - Different usefull utiles.

Please read according README.md file. 

## Knows limitation

### EntitySerializer
Entity serializer support only single type and array types without collection and other generics types

Because input and output data is not modifiable - array is the best way to resolve types.

But for multithreading the better to use immutable collections....



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

