# AMQP

This module provide functionality to receive and send AMQP messages and integrate it to
action engine.

# How to use:

First you need to implement [Connection](src/main/java/insideworld/engine/core/endpoint/amqp/connection/Connection.java)
interface or take already implemented based on another frameworks (see section Implementation bellow)

If you write your own implementation, need also implement next interfaces:

* [AmqpReceiver](src/main/java/insideworld/engine/core/endpoint/amqp/connection/AmqpReceiver.java) -
using to receive incoming messages.
* [AmqpSender](src/main/java/insideworld/engine/core/endpoint/amqp/connection/AmqpSender.java) - 
using to send messages.
* [Message](.src/main/java/insideworld/engine/amqp/connection/Message.java) - 
using to keep message information and payload.

Next need to integrate this connection to action engine:

For it, you need to create an instance of [AmqpActionReceiver](src/main/java/insideworld/engine/core/endpoint/amqp/actions/AmqpActionReceiver.java)
to receive message and also implement [AmqpActionSender](src/main/java/insideworld/engine/core/endpoint/amqp/actions/AmqpActionSender.java) 
to send callbacks. If call back is not required just set null.

You may use DI to create an instance of these classes. Just inheritance from it and all.
On startup of an application will call startUp method with specific order which establish connection
to set channels.

If you want to create without inheritance or in runtime, don't forget call startUp() method 
because channel or receive\send created in this method.

AMQP message should contain: 
* Action key as subject
* Body as JSON array of objects where each object will propagate to context.
* insideworld.engine.amqp.actions.tags.AmqpTags.CALLBACK_ACTION key in property to make callback (Optional).

## How it works:

Because AmqpActionReceiver based on AbstractActionReceiver for each element from received array will create
a separate thread.

Now you may use it.
## Example:
For examples see tests:

* [TestVertexConnection](./src/test/java/insideworld/engine/amqp/TestVertexConnection.java) - Create Vertx connection using DI.
* [TestSender](./src/test/java/insideworld/engine/amqp/TestSender.java) - Create instance for sender using DI.
* [TestReceiver](./src/test/java/insideworld/engine/amqp/TestReceiver.java) - Create instance receiver using DI.
* [TestAmqp](./src/test/java/insideworld/engine/amqp/TestAmqp.java) - Test example.

# Implementations

## Vertx
[VertxConnection](.\src\main\java\insideworld\engine\amqp\vertex\VertexConnection.java)

