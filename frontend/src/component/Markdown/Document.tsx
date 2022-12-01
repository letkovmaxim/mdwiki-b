import React, { useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import "../../css/document.css"
import {Markdown} from "./Markdown";

export const Document = () =>{

    const {spaceId} = useParams();
    const { pageId } = useParams();

    const[newDoc, setNewDoc] = useState(true);

    const[document, setDocument] = useState({
        text: ''
    });

    const[edit, setEdit] = useState(false);

    const CloseEdit = () => setEdit(false);
    const OpenEdit = () => setEdit(true);


    useEffect(() => {
         getText()
    }, [setDocument])

    async function getText(){
        let response = await fetch('/spaces/' + spaceId + '/pages/' + pageId + '/document');
        if(response.ok){
            let json = await response.json()
            setDocument({
                text: json.text
            })
            setNewDoc(false)
        }
    }

    async function save(){

        await fetch('/spaces/' + spaceId + '/pages/' + pageId + '/document', {
            method: (newDoc ? 'POST' : 'PUT'),
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
            <Markdown
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