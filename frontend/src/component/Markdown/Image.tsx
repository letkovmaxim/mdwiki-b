import React, { useEffect, useState} from "react";
import Button from "@mui/material/Button";
import Box from '@mui/material/Box';
import Modal from '@mui/material/Modal';
import {useParams} from "react-router-dom";
import {PhotoCamera} from "@mui/icons-material";
import {Input} from "reactstrap";
import {ImageList} from "@mui/material";
import Menu from "@mui/material/Menu";
import DeleteIcon from "@mui/icons-material/Delete";
import "../../css/document.css"


type Props ={
    image: (text:string) => void,
    open: any,
    handleClose: () => void
}

interface IComp {
    fileName: string,
    fileGUID: string,
    fileType: string,
    size: any
}

export const Image = ({image, open, handleClose}:Props) =>{

    const { spaceId } = useParams();

    const [loadImage, setLoadImage] = useState(true)
    const [linkImage, setLinkImage] = useState(false)
    const [allImage, setAllImage] = useState(false)
    const [path, setPath] = useState('')
    const [images, setImages] = useState<IComp[]>([])
    const [error, setError] = useState("")

    const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
    const openDel = Boolean(anchorEl);
    const [editGuid, setEditGuid] = useState("")

    useEffect(() => {
        getImages()
    }, [setImages])

    const getImages = async () => {
        let response = await fetch('/user/uploads?bunch=0&size=1000');
        let json = await response.json()
        setImages(json)
    }

    const ChangeLoadImage = () =>{
        setLoadImage(true)
        setLinkImage(false)
        setAllImage(false)
        setError("")
    }

    const ChangeLinkImage = () =>{
        setLoadImage(false)
        setLinkImage(true)
        setAllImage(false)
        setError("")
    }

    const ChangeAllImage = () =>{
        setLoadImage(false)
        setLinkImage(false)
        setAllImage(true)
        getImages()
    }

    const handleChange = (e:any) => {
        setPath(e.target.value);
    }

    const handleClick = (event: React.MouseEvent<HTMLButtonElement>, guid:string) => {
        setAnchorEl(event.currentTarget);
        setEditGuid(guid)
    };

    const handleCloseDel = () => {
        setAnchorEl(null);
    };

    const submitForm = async (event: any) => {
        event.preventDefault();
        let files = event.target.files;
        let formData = new FormData();
        formData.append("file", files[0]);
        formData.append("thumbnailHeight", '10');
        formData.append("thumbnailWidth", '10');
        await fetch('/spaces/' + spaceId +'/upload/image', {
            method: 'POST',
            headers: {
                'Accept': 'application/json'
            },
            body: formData
        }).then(async response => {
            if(response.ok){
                let json = await response.json()
                let res = await fetch('/download/image/' + json.fileGUID);
                image('\n![](' + res.url + ')')
                handleClose()
            }
        });
    };

    const submitLink = () => {
        if(!errorLink()){
            image('\n![](' + path + ')')
            setError("")
            handleClose()
            setPath("")
        }
    }

    const errorLink = () =>{
        if(!(/^(?:(?:(?:https?|ftp):)?\/\/)(?:\S+(?::\S*)?@)?(?:(?!(?:10|127)(?:\.\d{1,3}){3})(?!(?:169\.254|192\.168)(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)(?:\.(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)*(?:\.(?:[a-z\u00a1-\uffff]{2,})).?)(?::\d{2,5})?(?:[/?#]\S*)?$/i.test(path))){
            setError("Ссылка введена некорректно")
            return true
        }
        return  false

    }

    const addImage = (guid:any) => {
        image('\n![](http://localhost:3000/download/image/' + guid + ')')
    }

    const remove = async () => {
        await fetch('/delete/image/' + editGuid, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        });
        handleCloseDel()
        getImages()
    }

    const OList =
        <ImageList className="imagesPreview" cols={5} rowHeight={10}>
            {images.map((l: any) => (
                <div
                    key={l.fileGUID}
                    onContextMenu={(e) => {
                        e.preventDefault();
                        e.stopPropagation();
                    }}
                >
                    <Button
                        className='!h-[60px] !w-[100px]'
                        variant="text"
                        size="large"
                        onClick={()=> addImage(l.fileGUID)}
                        onContextMenu={(e) => handleClick(e, l.fileGUID)}
                    >
                        <img className='!h-[45px] !w-max-[80px]' src={'http://localhost:3000/download/thumbnail/'+l.fileGUID}/>
                    </Button>
                </div>
            ))}
        </ImageList>
    ;

    return (
        <div>
            <Modal
                open={open}
                onClose={handleClose}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
            >
                <Box className='imageBox'>
                    <Box className='imageBtns'>
                        {(loadImage ?
                            <Button className='imageBtn1' variant="contained">
                                Загрузить изображение
                            </Button>
                            :
                            <Button className="imageBtn2"  variant="text" onClick={ChangeLoadImage}>
                                Загрузить изображение
                            </Button>
                        )}
                        {(linkImage ?
                                <Button className='imageBtn1' variant="contained">
                                    Вставить ссылку
                                </Button>
                                :
                                <Button className="imageBtn2 !w-full" variant="text" onClick={ChangeLinkImage}>
                                    Вставить <br/> ссылку
                                </Button>
                        )}
                        {(allImage ?
                                <Button className='imageBtn1' variant="contained">
                                    Загруженные изображения
                                </Button>
                                :
                                <Button className="imageBtn2" variant="text" onClick={ChangeAllImage}>
                                    Загруженные изображения
                                </Button>
                        )}
                    </Box>
                    <Box className='ariaWork'>
                        {(loadImage ?
                          <div>
                              <div className='rectangle'/>
                              <Button variant="text" className='tracing' color="primary" aria-label="upload picture" component="label" size="large">
                                  <input hidden accept="image/*" type="file" onChange={submitForm} />
                                  <PhotoCamera className='cameraI' />
                              </Button>
                          </div>
                          :
                          <div/>
                        )}
                        {(linkImage ?
                          <div>
                              <Input
                                  className='addPathNew'
                                  type="text"
                                  name="name"
                                  id="name"
                                  value={path}
                                  placeholder={'Ссылка'}
                                  onChange={handleChange}
                                  autoComplete="off"
                              />
                              <div className='errorPath'>
                                  {error}
                              </div>
                              <Button className='pathBtn' variant="contained" onClick={submitLink}>
                                  Добавить
                              </Button>
                          </div>
                          :
                          <div/>
                        )}
                        {(allImage ?
                            <div>
                                {OList}
                            </div>
                           :
                           <div/>
                        )}
                    </Box>
                </Box>
            </Modal>
            <Menu
                id="basic-menu"
                anchorEl={anchorEl}
                open={openDel}
                onClose={handleCloseDel}
                MenuListProps={{
                    'aria-labelledby': 'basic-button',
                }}
            >
                <Button
                    className='delImage'
                    variant="text"
                    onClick={remove}
                >
                    <DeleteIcon className="!text-xs !text-slate-500"/>
                    &emsp;
                    Удалить
                </Button>
            </Menu>
        </div>
    );
}