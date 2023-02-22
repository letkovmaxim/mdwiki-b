import {CLEAN_PAGES, GET_PAGES, GET_SPACES, IS_LOGIN, IS_NOT_LOGIN, OPEN_SPACE, SPACE_NAME, TREE_PAGES} from "./types";
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
        const json = await response.json();
        dispatch({type: GET_PAGES, payload: json})
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