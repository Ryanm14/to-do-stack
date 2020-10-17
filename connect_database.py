from cassandra.cluster import Cluster
from cassandra.auth import PlainTextAuthProvider
from dotenv import load_dotenv


load_dotenv()

from os import path, getenv

user_name = getenv("USER_NAME")
password = getenv("PASSWORD")
keyspace = getenv("KEYSPACE")

base_dir = path.dirname((path.realpath(__file__)))


def get_session():
    cloud_config = {
        'secure_connect_bundle': path.join(base_dir, "creds", "secure-connect-todo-cloud.zip")
    }
    auth_provider = PlainTextAuthProvider(user_name, password)
    cluster = Cluster(cloud=cloud_config, auth_provider=auth_provider)
    session = cluster.connect(keyspace)
    return session


def connect_datastax():
    session = get_session()
    row = session.execute("select release_version from system.local").one()
    if row:
        print(row[0])
    else:
        print("An error occurred")
    session.execute("""
                CREATE TABLE IF NOT EXISTS task_keyspace.Task (
                id uuid,
                title text,
                deadline text,
                estimatedTimeHours int,
                estimatedTimeMinutes int,
                difficulty float,
                importance float,
                completed text,
                PRIMARY KEY (id))
                """
                    )


