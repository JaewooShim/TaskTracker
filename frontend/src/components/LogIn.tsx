import { useEffect, useState } from "react";
import axios from "../../node_modules/axios/index";
import { useNavigate } from "../../node_modules/react-router-dom/dist/index";
import { useAppContext } from "../AppProvider";

const LogIn = () => {
  const { state } = useAppContext();
  const navigate = useNavigate();

  const onClick = () => {
    if (state.isLoggedIn) {
      navigate("/task-lists")
    } else {
      navigate("/login")
    }
  };

  return (
    <div className="flex flex-col items-center justify-center">
      <h1 className="text-4xl font-bold mb-4">Welcome to Task Tracker</h1>
      <p>
        Organize your tasks effortlessly. Login with your favorite provider and get started.
      </p>
      <br />
      <button
        onClick={onClick}
        className="bg-blue-600 text-white px-6 py-3 rounded-md text-lg hover:bg-blue-700"
      >
        Get Started
      </button>
    </div>
  );
};

export default LogIn;
