import status as status
from flask import Flask, request
import connect_database
import uuid
from flask_api import status
import json

from models.Task import Task

application = Flask(__name__)


@application.route('/')
def hello_world():
    return 'Hello World!'


@application.route('/add_tasks', methods=['GET', 'POST'])
def add_tasks():
    properties = request.get_json()
    print(properties)
    id = uuid.UUID(properties['id'])
    title = properties['title']
    deadline = properties['deadline']
    estimatedTimeHours = int(properties['estimatedTimeHours'])
    estimatedTimeMinutes = int(properties['estimatedTimeMinutes'])
    difficulty = float(properties['difficulty'])
    importance = float(properties['importance'])
    completed = properties['completed']
    session = connect_database.get_session()
    session.execute("""
                    INSERT INTO Task (id, title, deadline, estimatedTimeHours, estimatedTimeMinutes, difficulty, importance, completed)
                    VALUES (%s,%s,%s,%s,%s,%s,%s,%s)
                    """,
                    [id, title, deadline, estimatedTimeHours, estimatedTimeMinutes, difficulty, importance, completed]
                    )
    return "Task added to database", status.HTTP_200_OK

@application.route('/get_all_tasks', methods=['GET'])
def get_all_tasks():
    session = connect_database.get_session()
    result = session.execute("SELECT * FROM Task")
    tasks = []
    for r in result:
        tasks.append(
            Task(
                str(r[0]),
                r.title,
                r.deadline,
                r.estimatedtimehours,
                r.estimatedtimeminutes,
                r.difficulty,
                r.importance,
                r.completed
            ).serialize()
        )

    return json.dumps(tasks), status.HTTP_200_OK


@application.route('/complete_task', methods=['GET', 'POST'])
def complete_task():
    session = connect_database.get_session()
    properties = request.get_json()
    id = uuid.UUID(properties['id'])
    prepared = session.prepare("UPDATE Task SET completed = ? WHERE id = ?")
    session.execute(prepared, ["true", id])
    return "Task updated successfully", status.HTTP_200_OK


if __name__ == '__main__':
    connect_database.connect_datastax()
    application.run()
