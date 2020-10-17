class Task:

    def __init__(self, id, title, deadline, estimatedTimeHours, estimatedTimeMinutes, difficulty, importance, completed):
        self.id = id
        self.title = title
        self.deadline = deadline
        self.estimatedTimeHours = estimatedTimeHours
        self.estimatedTimeMinutes = estimatedTimeMinutes
        self.difficulty = difficulty
        self.importance = importance
        self.completed = completed

    def serialize(self):
        return {
            "id": self.id,
            "title": self.title,
            "deadline": self.deadline,
            "estimatedTimeHours" : self.estimatedTimeHours,
            "estimatedTimeMinutes": self.estimatedTimeMinutes,
            "difficulty": self.difficulty,
            "importance": self.importance,
            "completed": self.completed
        }