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
import {Dispatch} from "redux";

export function personLogIn(){
    return{
        type: IS_LOGIN,
        payload: true
    }
}

export function logOut(){
    return{
        type: IS_NOT_LOGIN,
        payload: false
    }
}

export function getSpace(){
     return async (dispatch:Dispatch) => {
        const response = await fetch('/spaces?bunch=0&size=1000');
        const json = await response.json();
        dispatch({type: GET_SPACES, payload:json})
    }
}

export function spaceNameAndShared(name:string, shared:boolean){
    return{
        type: SPACE_NAME,
        payload: {name, shared}
    }
}

export function openSpace(id:string){
    return{
        type: OPEN_SPACE,
        payload: id
    }
}

export function getPages(idSpace:number){
    return async (dispatch:Dispatch) => {
        let response = await fetch('/spaces/' + idSpace + '/pages?bunch=0&size=1000');
        if(response.ok){
            const json = await response.json();
            dispatch({type: GET_PAGES, payload: json})
        }
    }
}

export function cleanPages(){
    return{
        type: CLEAN_PAGES
    }
}

export function treePages(tree:string[]){
    return{
        type: TREE_PAGES,
        payload: tree
    }
}

export function isError(){
    return{
        type: IS_ERROR,
        payload: true
    }
}

export function isNotError(){
    return{
        type: IS_NOT_ERROR,
        payload: false
    }
}

export function namePage(name:string){
    return{
        type: NAME_PAGE,
        payload: name
    }
}