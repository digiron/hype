import {createAsyncThunk, createSlice} from '@reduxjs/toolkit';

import {fetchWrapper} from 'shared';

// create slice

const name = 'users';
const initialState = createInitialState();
const extraActions = createExtraActions();
const extraReducers = createExtraReducers();
const slice = createSlice({ name, initialState, extraReducers });


export const userActions = { ...slice.actions, ...extraActions };
export const usersReducer = slice.reducer;


function createInitialState() {
    return {
        users: {}
    }
}

function createExtraActions() {
    return {
        getAll: getAll()
    };    

    function getAll() {
        return createAsyncThunk(
            `${name}/getAll`,
            async () => await fetchWrapper.get(`${process.env.REACT_APP_API_URL}/users`)
        );
    }
}

function createExtraReducers() {
    return {
        ...getAll()
    };

    function getAll() {
        var { pending, fulfilled, rejected } = extraActions.getAll;
        return {
            [pending]: (state) => {
                state.users = { loading: true };
            },
            [fulfilled]: (state, action) => {
                state.users = action.payload;
            },
            [rejected]: (state, action) => {
                state.users = { error: action.error };
            }
        };
    }
}
