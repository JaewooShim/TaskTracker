import "./App.css";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import TaskLists from "./components/TaskListsScreen";
import CreateUpdateTaskListScreen from "./components/CreateUpdateTaskListScreen";
import TaskListScreen from "./components/TasksScreen";
import CreateUpdateTaskScreen from "./components/CreateUpdateTaskScreen";
import LogIn from "./components/LogIn";
import LogInPage from "./components/LogInPage";
import Navbar from "./components/Navbar";

function App() {
  return (
    <BrowserRouter>
    <Navbar />
      <Routes>
        <Route path="/" element={<LogIn />} />
        <Route path="/login" element={<LogInPage />} />
        <Route path="/task-lists" element={<TaskLists />} />
        <Route path="/new-task-list" element={<CreateUpdateTaskListScreen />} />
        <Route
          path="/edit-task-list/:listId"
          element={<CreateUpdateTaskListScreen />}
        />
        <Route path="/task-lists/:listId" element={<TaskListScreen />} />
        <Route
          path="/task-lists/:listId/new-task"
          element={<CreateUpdateTaskScreen />}
        />
        <Route
          path="/task-lists/:listId/edit-task/:taskId"
          element={<CreateUpdateTaskScreen />}
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
