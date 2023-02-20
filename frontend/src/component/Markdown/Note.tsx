import React, { useEffect, useState} from "react";
import "../../css/document.css"
import { MarkdowNote} from "./MarkdowNote";
import {useDispatch} from "react-redux";
import {logOut} from "../../redux/actions";

export const Note = () =>{

    const dispatch = useDispatch()

    const[document, setDocument] = useState({
        text: ''
    });

    const[idPerson, setIdPerson] = useState(0)

    const[edit, setEdit] = useState(false);


    const CloseEdit = () => setEdit(false);
    const OpenEdit = () => setEdit(true);


    useEffect(() => {
        getText()
    }, [setDocument])

    async function getText(){
        let response = await fetch("/auth/whoami");
        if(response.ok){
            let json = await response.json()
            setIdPerson(json.id)
            setDocument({
                text: json.note
            })
        }else {
            dispatch(logOut())
        }
    }

    async function save(){

        await fetch('/people/' + idPerson + '/note', {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(document),
        })
    }

    const newText = (val:any) => {
        setDocument({text: val!})
    }

    return (
        <div data-color-mode="light">
            <MarkdowNote
                save={save}
                edit={edit}
                CloseEdit={CloseEdit}
                OpenEdit={OpenEdit}
                document={document}
                newText={newText}
            />
        </div>
    );
}