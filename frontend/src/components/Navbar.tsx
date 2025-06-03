// components/Navbar.tsx
import axios from "../../node_modules/axios/index";
import { useNavigate } from "../../node_modules/react-router-dom/dist/index";
import { useAppContext } from "../AppProvider";
import { LucideHome } from "../../node_modules/lucide-react/dist/lucide-react";

const Navbar = () => {
  const { state } = useAppContext();
  const navigate = useNavigate();

  const handleLogout = async () => {
    axios
      .post("http://localhost:8080/api/util/logout", null, {
        withCredentials: true,
      })
      .then((res) => {
        console.log(res.data);
        window.location.href = "http://localhost:5173/";
      })
      .catch((error) => console.log(error));
  };

  return (
    <nav className="fixed top-0 right-0 p-4">
      <div className="flex items-center gap-4">
        <button
          className="text-sm text-blue-500 hover:underline"
          onClick={() => navigate("/")}
        >
          Home
        </button>
        {state.isLoggedIn ? (
          <button
            className="text-sm px-4 py-2 rounded bg-red-500 text-white hover:bg-red-600"
            onClick={handleLogout}
          >
            Logout
          </button>
        ) : (
          <button
            className="text-sm px-4 py-2 rounded bg-blue-500 text-white hover:bg-blue-600"
            onClick={() => navigate("/login")}
          >
            Login
          </button>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
