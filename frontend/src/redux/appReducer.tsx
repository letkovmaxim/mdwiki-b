import {CLEAN_PAGES, GET_PAGES, GET_SPACES, IS_LOGIN, IS_NOT_LOGIN, OPEN_SPACE, SPACE_NAME, TREE_PAGES} from "./types";

const initialState = {
    spaces: [],
    spaceName: '',
    openSpace: '',
    pages: [],
    login: false,
    tree: []
}

const defaultState = {
    spaces: [],
    spaceName: '',
    openSpace: '',
    pages: [],
    login: false,
    tree: []
}

export const appReducer = (state = initialState, action:any) => {
    switch (action.type){
        case IS_LOGIN:
            return {...state, login: action.payload}
        case IS_NOT_LOGIN:
            return {...defaultState}
        case GET_SPACES:
            return {...state, spaces: action.payload}
        case SPACE_NAME:
            return {...state, spaceName: action.payload}
        case OPEN_SPACE:
            return {...state, openSpace: action.payload}
        case GET_PAGES:
            return {...state, pages: action.payload}
        case CLEAN_PAGES:
            return {...state, pages: []}
        case TREE_PAGES:
            return {...state, tree: action.payload}
        default: return state
    }
}