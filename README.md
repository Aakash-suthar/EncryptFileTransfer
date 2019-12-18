# EncryptFileTransfer (SSLSocket + MAchineID Authentication Transfer) [JAVA]

FLow of the program

Files are transfer one by one.
First Server send the secret machine/system id to the client over ssl connection.
Client receives the id and encrypted all files with the key and send them one by one.
Server receives the files and then decrypted it using the machine/system id.

To run first you need to change the path of source and destination in ClientMain.java file then recompile it.
Make sure the source directory exist with files and destination directory is optional it will be created automatically if its not there.
Then run the server first ann after that client.

