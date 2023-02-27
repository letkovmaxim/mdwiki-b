import {
    CLEAN_PAGES,
    GET_PAGES,
    GET_SPACES,
    IS_ERROR,
    IS_LOGIN, IS_NOT_ERROR,
    IS_NOT_LOGIN, NAME_PAGE,
    OPEN_SPACE,
    SPACE_NAME,
    TREE_PAGES
} from "./types";

const initialState = {
    spaces: [],
    space: {
        name: '',
        shared: true
    },
    openSpace: '',
    pages: [],
    login: false,
    tree: [],
    error: false,
    namePage: ''
}

const defaultState = {
    spaces: [],
    space: {
        name: '',
        shared: true
    },
    openSpace: '',
    pages: [],
    login: false,
    tree: [],
    error: false,
    namePage: ''
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
            return {...state, space: action.payload}
        case OPEN_SPACE:
            return {...state, openSpace: action.payload}
        case GET_PAGES:
            return {...state, pages: action.payload}
        case CLEAN_PAGES:
            return {...state, pages: []}
        case TREE_PAGES:
            return {...state, tree: action.payload}
        case IS_ERROR:
            return {...state, error: action.payload}
        case IS_NOT_ERROR:
            return {...state, error: action.payload}
        case NAME_PAGE:
            return {...state, namePage: action.payload}
        default: return state
    }
}