import {createAsyncThunk, createSlice} from '@reduxjs/toolkit';

import {fetchWrapper, history} from 'shared';

const name = 'auth';
const initialState = createInitialState();
const reducers = createReducers();
const extraActions = createExtraActions();
const extraReducers = createExtraReducers();
const slice = createSlice({ name, initialState, reducers, extraReducers });

// exports

export const authActions = { ...slice.actions, ...extraActions };
export const authReducer = slice.reducer;

// implementation

function createInitialState() {
    return {
        // initialize state from local storage to enable user to stay logged in
        user: null,
        error: null
    }
}

function createReducers() {
    return {
        logout
    };

    function logout(state) {
        state.user = null;
        history.navigate('/login');
    }
}

function createExtraActions() {

    return {
        login: login()
    };    

    function login() {
        return createAsyncThunk(
            `${name}/login`,
            async ({ username, password }) => await fetchWrapper.post(`${process.env.REACT_APP_API_URL}/auth/signin`, { username, password })
        );
    }
}

function createExtraReducers() {
    return {
        ...login()
    };

    function login() {
        let { pending, fulfilled, rejected } = extraActions.login;
        return {
            [pending]: (state) => {
                state.error = null;
            },
            [fulfilled]: (state, action) => {
                // set the jwt token on state
                state.user = action.payload;
                history.navigate('/');
            },
            [rejected]: (state, action) => {
                state.error = action.error;
            }
        };
    }
}
