Author : Jérémie Melly



## What transport protocol do we use ?

TCP as we'll need a stable connection between the client and the server.

## How does the client find the server (addresses and ports)?

With a broadcast message on the network? Or by manual configuration?

## Who speaks first ?

The client, as the server can't be aware of all the potential clients.


## What is the sequence of messages exchanged by the client and the server ? (flow) 

| ***Client***                  | ***ch.heigvd.melmo.Server.Server***                    |
| ----------------------------- | ------------------------------- |
| 1. Broadcast message          | 2. Answer with port number      |
| 3. Asks for connection        | 4. Accepts or refuses connection|
| 5. Sends math operation       | 6. Returns the result | an error|
| 7. Ask to stop the connection | 8. Closes the connection        |



## What happens when a message is received from the other party? (semantics)

The server will parse the header, check the informations, if they are correct it'll read the body, execute the operation and returns.


## What is the syntax of the messages ? How we generate and parse them ?

| ***Message***                | ***Description***                                            |
| ---------------------------- | ------------------------------------------------------------ |
| WHOIS                        | *client* Ask for server IP and port on the network           |
| HELLO                        | *client* Ask connection                                      |
| ACK                          | *server* Confirm connection                                  |
| OPERATION code value1 value2 | *client* Ask server to the operation (OPERATION add 1 2)     |
| RESULT value                 | *server* Return the result                                   |
| ERROR error_code             | *server* Return error message with error code                |
| QUIT                         | *client*  Ask to end the process                             |



## Who closes the connection and when ?

The server, on timeout or on QUIT message