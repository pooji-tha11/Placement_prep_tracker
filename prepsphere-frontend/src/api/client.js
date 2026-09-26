const BASE_URL = "http://localhost:8080/api";

async function request(path, options = {}) {
  const isFormData = options.body instanceof FormData;
  
  const fetchOptions = {
    ...options,
    headers: {
      ...(!isFormData && { "Content-Type": "application/json" }),
      ...options.headers,
    },
  };

  const res = await fetch(`${BASE_URL}${path}`, fetchOptions);
  if (!res.ok) {
    const message = await res.text();
    throw new Error(message || `Request failed: ${res.status}`);
  }
  const text = await res.text();
  return text ? JSON.parse(text) : null;
}

export const api = {
  get: (path) => request(path),
  post: (path, body) => request(path, { method: "POST", body: body instanceof FormData ? body : JSON.stringify(body) }),
  patch: (path, body) => request(path, { method: "PATCH", body: body instanceof FormData ? body : JSON.stringify(body) }),
  put: (path, body) => request(path, { method: "PUT", body: body instanceof FormData ? body : JSON.stringify(body) }),
  del: (path) => request(path, { method: "DELETE" }),
};
