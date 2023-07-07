export const fetchWrapper = {
    get: request('GET'),
    post: request('POST'),
    put: request('PUT'),
    delete: request('DELETE')
};

function request(method) {
    return (url, body) => {
        const requestOptions = {
            method,
            //todo: make cors work so we can get jwt token
            // credentials: 'include',
            headers: {
                "Content-Type": "application/json",
            }
        };
        if (body) {
            requestOptions.body = JSON.stringify(body);
        }
        return fetch(url, requestOptions).then(handleResponse);
    }
}

function handleResponse(response) {
    // todo get cookie from header to apply to future api calls
    return response.json().then(data => {
        if (!response.ok) {
            const error = (data && data.message) || response.statusText;
            return Promise.reject(error);
        }
        return data;
    });
}