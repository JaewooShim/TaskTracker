import axios from "../../node_modules/axios/index";

const authApi = axios.create({
    headers: {"Content-Type": "application/json"},
    withCredentials: true
});

authApi.interceptors.response.use(
    (res) => res,
    async (error) => {
        const originalRequest = error.config;
        const message = error.response?.data;
        const status = error.response?.status;

        if (status === 401 && message === "Access token expired" && !originalRequest._retry) {
            originalRequest._retry = true;
            try {
              await authApi.post("/api/util/token-reissue");
              return authApi(originalRequest); // Retry original request
            } catch (reissueError) {
                console.log("trying to find the error", reissueError);
                return Promise.reject(reissueError);
            }
        }
        return Promise.reject(error);
    }
);

export default authApi;