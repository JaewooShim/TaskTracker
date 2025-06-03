import axios from "../../node_modules/axios/index";

const apiClient = axios.create({
    headers: {"Content-Type": "application/json"},
    withCredentials: true
});

apiClient.interceptors.response.use(
    (res) => res,
    async (error) => {
        const originalRequest = error.config;
        const message = error.response?.data;
        const status = error.response?.status;

        if (status === 401 && message === "Access token expired" && !originalRequest._retry) {
            originalRequest._retry = true;
            try {
              await apiClient.post("/api/util/token-reissue");
              return apiClient(originalRequest); // Retry original request
            } catch (reissueError) {
                redirectToLogin();
                return Promise.reject(reissueError);
            }
        }
        redirectToLogin();
        return Promise.reject(error);
    }
);

const redirectToLogin = () => {
    window.location.href = "/login"
}
export default apiClient;