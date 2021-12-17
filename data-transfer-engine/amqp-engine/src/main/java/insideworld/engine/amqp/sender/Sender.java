/*
 * Copyright
 * This code is property of Anton Eliseev.
 * If you get this code looks like you hack my private repository and don't see this text.
 *
 */

package insideworld.engine.amqp.sender;

/**
 * Interface for send AMQP message.
 *
 * @since 0.3.0
 */
public interface Sender<T> {

    /**
     * Send message.
     * @param data Data to send.
     */
    void send(T data) throws Exception;

}
