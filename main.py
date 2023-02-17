import socket

class Client:
    def __init__(self, conn, addr, rank):
        self.conn = conn
        self.addr = addr
        self.rank = rank

    def send(self, msg):
        self.conn.send(msg.encode())

class Server:
    def __init__(self, max_clients):
        self.max_clients = max_clients
        self.clients = []
        self.next_rank = 0
        self.server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    def start(self, host, port):
        self.server_socket.bind((host, port))
        self.server_socket.listen(self.max_clients)
        print(f"Server listening on {host}:{port}")

        while True:
            conn, addr = self.server_socket.accept()
            client = Client(conn, addr, self.next_rank)
            self.clients.append(client)
            self.next_rank += 1
            print(f"New client connected: {addr}, rank {client.rank}")
            self.handle_client(client)

    def handle_client(self, client):
        while True:
            data = client.conn.recv(1024).decode().strip()
            if not data:
                print(f"Client {client.rank} disconnected")
                self.clients.remove(client)
                self.adjust_ranks()
                break
            cmd, *args = data.split()
            if cmd == "promote":
                self.promote_client(client)
            else:
                self.execute_command(client, cmd, args)

    def execute_command(self, client, cmd, args):
        for c in self.clients:
            if c.rank < client.rank:
                c.send(f"Client {client.rank} sends command '{cmd}' with args {args}")
        print(f"Client {client.rank} executes command '{cmd}' with args {args}")

    def promote_client(self, client):
        if client.rank == 0:
            print("Client already has highest rank")
            return
        for c in self.clients:
            if c.rank == client.rank - 1:
                print(f"Client {client.rank} promoted to rank {c.rank}")
                client.rank = c.rank
                c.rank += 1
                return
        raise Exception("Unable to promote client")

    def adjust_ranks(self):
        for i, c in enumerate(self.clients):
            c.rank = i

server = Server(5)
server.start("localhost", 8888)
