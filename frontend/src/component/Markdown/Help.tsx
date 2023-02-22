 import * as React from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import Modal from '@mui/material/Modal';
import QuestionMarkIcon from '@mui/icons-material/QuestionMark';
import MDEditor from "@uiw/react-md-editor";
import "../../css/document.css"


export default function Help() {
    const [open, setOpen] = React.useState(false);
    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);

    const Markdown = (text:string) =>{
        return(
            <MDEditor.Markdown
                source={text}
            />
        )
    }

    return (
        <div>
            <Button className='helpBtn' variant="text" onClick={handleOpen}>
                <QuestionMarkIcon className='helpIcon'/>
                &emsp;
                <div className='mt-[2px]'>
                    Помощь
                </div>
            </Button>
            <Modal
                open={open}
                onClose={handleClose}
                aria-labelledby="modal-modal-title"
                aria-describedby="modal-modal-description"
            >
                <Box className='helpModalWindow' data-color-mode="light">
                    <Box className='documentation'>
                        <Typography id="modal-modal-title" variant="h6" component="h2">
                            Что такое Wiki-service
                        </Typography>
                        <Typography id="modal-modal-description" sx={{ mt: 2 }}>
                            Данный веб-сайт предназначен для создания markdown документов, у каждого пользователя есть пространства,
                            в которых будут храниться его страницы, которые как раз и будут являться markdown документами. Каждая страница может содержаться любое количество под страниц,
                            которые будут выстраиваться в виде дерева. Так же у пользователя будет дефолтная страница «ЗАПИСКИ»,
                            которую нельзя удалить и в которой пользователь может написать что-то о себе или просто писать заметки, касающиеся его работ.
                        </Typography>
                        <br/>
                        <Typography id="modal-modal-title" variant="h6" component="h2">
                            Что такое markdown
                        </Typography>
                        <Typography  id="modal-modal-description" sx={{ mt: 2 }}>
                            Markdown – это облегченный язык разметки, который является инструментом преобразования кода в HTML.
                            Главной особенностью данного языка является максимально простой синтаксис, который служит для упрощения написания и чтения кода разметки,
                            что, в свою очередь, позволяет легко его корректировать. Теперь рассмотрим более подробно функции языка разметки Markdown.
                        </Typography>
                        <br/>
                        <Typography id="modal-modal-title" variant="h6" component="h2">
                            Параграфы и разрывы строк
                        </Typography>
                        <Typography id="modal-modal-description" sx={{ mt: 2 }}>
                            Для того, чтобы создать параграф с использованием синтаксиса языка Markdown,
                            достаточно отделить строки текста одной (или более) пустой строкой. Для того,
                            чтобы вставить видимый перенос строки необходимо окончить строку двумя пробелами и нажатием клавиши «Enter».
                        </Typography>
                        <br/>
                        <Typography id="modal-modal-title" variant="h6" component="h2">
                            Заголовки
                        </Typography>
                        <Typography  id="modal-modal-description" sx={{ mt: 2 }}>
                            Язык разметки Markdown поддерживает обозначения заголовков: выделение символом («#»).
                            Кроме того, заголовок возможно снабдить закрывающимися символами («#»), хотя это и не является обязательным.
                            Количество закрывающихся символов не обязано соответствовать количеству начальных символов.
                            Уровень заголовка определяется по количеству начальных символов.
                        </Typography>
                        <Typography sx={{ mt: 2 }}>
                            Пример.
                            <br/>
                            <div className='example'>
                                # Заголовок первого уровня
                                <br/>
                                ### Заголовок третьего уровня
                                <br/>
                                ###### Заголовок шестого уровня
                            </div>
                            <br/>
                            Результат в markdown
                            <br/>
                            {Markdown("#  Заголовок первого уровня  \n" +
                                "### Заголовок третьего уровня  \n" +
                                "###### Заголовок шестого уровня")}
                        </Typography>
                        <br/>
                        <Typography id="modal-modal-title" variant="h6" component="h2">
                            Выделение текста
                        </Typography>
                        <Typography  id="modal-modal-description" sx={{ mt: 2 }}>
                            При помощи символа («*») в Markdown текст можно выделить по разному, текст, окруженный одинарными символами,
                            выделяется курсивным шрифтом, текст, окруженный двойными символами, выделяется полужирным шрифтом,
                            а текст, окруженный тройными символами, выделяется полужирным и курсивным шрифтом.<br/>
                            Чтобы сделать текст зачеркнутым, его нужно заключить в двойные («~»).
                        </Typography>
                        <Typography sx={{ mt: 2 }}>
                            Пример 1.
                            <br/>
                            <div className='example'>
                                *Hello world!!!*<br/>
                                **Hello world!!!**<br/>
                                ***Hello world!!!***<br/>
                            </div>
                            <br/>
                            Результат в markdown
                            <br/>
                            {Markdown(" *Hello world!!!*  \n" +
                                "**Hello world!!!**  \n" +
                                "***Hello world!!!***")}
                            <br/>
                            Пример 2.
                            <br/>
                            <div className='example'>
                                ~~Hello world!!!~~<br/>

                            </div>
                            <br/>
                            Результат в markdown
                            <br/>
                            {Markdown(" ~~Hello world!!!~~" )}
                        </Typography>
                        <br/>
                        <Typography id="modal-modal-title" variant="h6" component="h2">
                            Цитаты
                        </Typography>
                        <Typography  id="modal-modal-description" sx={{ mt: 2 }}>
                            Для обозначения цитат в языке Markdown используется знак «больше» («&gt;»). Его можно вставлять как перед каждой строкой цитаты,
                            так и только перед первой строкой параграфа. Также синтаксис Markdown позволяет создавать вложенные цитаты.
                            Для их разметки используются дополнительные уровни знаков цитирования («&gt;»).
                        </Typography>
                        <Typography sx={{ mt: 2 }}>
                            Пример 1.
                            <br/>
                            <div className='example'>
                                &gt;Цитата<br/>
                                &gt;номер<br/>
                                &gt;один.<br/>
                                <br/>
                                &gt;Цитата<br/>
                                номер<br/>
                                два.<br/>
                                <br/>
                                &gt;Цитата номер три.<br/>
                            </div>
                            <br/>
                            Результат в markdown
                            <br/>
                            {Markdown(">Цитата\n" +
                                ">номер\n" +
                                ">один.\n" +
                                "\n" +
                                ">Цитата\n" +
                                "номер \n" +
                                "два.\n" +
                                "\n" +
                                ">Цитата номер три.")}
                            <br/>
                            Пример 2.
                            <br/>
                            <div className='example'>
                                &gt;Первый уровень<br/>
                                &gt;&gt;Второй уровень<br/>
                                &gt;&gt;&gt;Третий уровень<br/>
                                &gt;<br/>
                                &gt;Первый уровень<br/>
                            </div>
                            <br/>
                            Результат в markdown
                            <br/>
                            {Markdown("> Первый уровень\n" +
                                ">> Второй уровень\n" +
                                ">>> Третий уровень\n" +
                                ">\n" +
                                ">Первый уровень")}
                        </Typography>
                        <br/>
                        <Typography id="modal-modal-title" variant="h6" component="h2">
                            Списки
                        </Typography>
                        <Typography  id="modal-modal-description" sx={{ mt: 2 }}>
                            Markdown поддерживает упорядоченные и неупорядоченные списки.
                            Для формирования неупорядоченный списков используются такие маркеры, как звездочки, плюсы и дефисы.
                            Все перечисленные маркеры могут использоваться взаимозаменяемо.
                            Для формирования упорядоченных списков в качестве маркеров используются числа с точкой.
                        </Typography>
                        <Typography sx={{ mt: 2 }}>
                            Пример 1.
                            <br/>
                            <div className='example'>
                                * Java<br/>
                                + Python<br/>
                                - Pascal<br/>
                            </div>
                            <br/>
                            Результат в markdown
                            <br/>
                            {Markdown("* Java\n" +
                                "+ Python\n" +
                                "- Pascal\n")}
                            <br/>
                            Пример 2.
                            <br/>
                            <div className='example'>
                                1. Java<br/>
                                2. Python<br/>
                                3. Pascal<br/>
                            </div>
                            <br/>
                            Результат в markdown
                            <br/>
                            {Markdown("1. Java\n" +
                                "2. Python\n" +
                                "3. Pascal\n")}
                        </Typography>
                        <br/>
                        <Typography id="modal-modal-title" variant="h6" component="h2">
                            Блоки кода
                        </Typography>
                        <Typography  id="modal-modal-description" sx={{ mt: 2 }}>
                            Отформатированные блоки кода используются в случае необходимости процитировать исходный код программ или разметки.
                            Для создания блока кода в языке Markdown необходимо код поместить в («` `»), если он состоит из одной строчки или («``` ```»),
                            если из нескольких строк.
                        </Typography>
                        <Typography sx={{ mt: 2 }}>
                            Пример 1.
                            <br/>
                            <div className='example'>
                                `print("Hello world!!!");`<br/>
                            </div>
                            <br/>
                            Результат в markdown
                            <br/>
                            {Markdown("`print(\"Hello world!!!\");`")}
                            <br/>
                            Пример 2.
                            <br/>
                            <div className='example'>
                                ```<br/>
                                print("Hello world!!!");<br/>
                                print("Hello world!!!");<br/>
                                print("Hello world!!!");<br/>
                                print("Hello world!!!");<br/>
                                ```<br/>
                            </div>
                            <br/>
                            Результат в markdown
                            <br/>
                            {Markdown("```\n" +
                                "print(\"Hello world!!!\");\n" +
                                "print(\"Hello world!!!\");\n" +
                                "print(\"Hello world!!!\");\n" +
                                "print(\"Hello world!!!\");\n" +
                                "```\n")}
                        </Typography>
                        <br/>
                        <Typography id="modal-modal-title" variant="h6" component="h2">
                            Горизонтальные линии (разделители)
                        </Typography>
                        <Typography  id="modal-modal-description" sx={{ mt: 2 }}>
                            Для того чтобы создать горизонтальную линию с использованием синтаксиса языка Markdown,
                            необходимо поместить три (или более) дефиса или звездочки на отдельной строке текста.
                            Между ними возможно располагать пробелы.
                        </Typography>
                        <Typography sx={{ mt: 2 }}>
                            Пример 1.
                            <br/>
                            <div className='example'>
                                Hello world!!!<br/>
                                <br/>
                                ----<br/>
                            </div>
                            <br/>
                            Результат в markdown
                            <br/>
                            {Markdown("Hello world!!!\n" +
                                "\n" +
                                "----\n")}
                            <br/>
                            Пример 2.
                            <br/>
                            <div className='example'>
                                Hello world!!!<br/>
                                ****<br/>
                            </div>
                            <br/>
                            Результат в markdown
                            <br/>
                            {Markdown("Hello world!!!\n" +
                                "****\n")}
                            <br/>
                            <b>Важно!</b><br/>
                            При использовании данного инструмента важно помнить, что после текста и перед ---- нужно оставлять пустую строку.
                            При использовании символа звездочки данным правилом можно пренебречь.
                        </Typography>
                        <br/>
                        <Typography id="modal-modal-title" variant="h6" component="h2">
                            Таблицы
                        </Typography>
                        <Typography  id="modal-modal-description" sx={{ mt: 2 }}>
                            Для создания таблицы в Markdown мы используем тире («-») и вертикальные полосы («|») для разделения строк и столбцов.
                            В первой строке таблицы мы строим заголовок, разделяя эту строку тремя или более дефисами ---.
                            Разделение столбцов выполняется с помощью вертикальной черты («|»)
                        </Typography>
                        <Typography sx={{ mt: 2 }}>
                            Пример.
                            <br/>
                            <div className='example'>
                                | Заголовок | Заголовок |<br/>
                                |  -------  |  -------  |<br/>
                                |   Текст   |   Текст   |<br/>
                                |   Текст   |   Текст   |<br/>
                            </div>
                            <br/>
                            Результат в markdown
                            <br/>
                            {Markdown("| Заголовок  | Заголовок   |\n" +
                                "| ------- | -------- |\n" +
                                "| Текст   | Текст    |\n" +
                                "| Текст   | Текст    |")}
                        </Typography>
                        <br/>
                        <Typography id="modal-modal-title" variant="h6" component="h2">
                            Ссылки
                        </Typography>
                        <Typography  id="modal-modal-description" sx={{ mt: 2 }}>
                            Для того чтобы создать ссылку в Markdown нужно поместить текст ссылки в квадратные скобки.
                            Для создания внутритекстовой гиперссылки необходимо использовать круглые скобки сразу после закрывающей квадратной и поместить в них ссылку.
                            В них же возможно расположить название, заключенное в кавычки,
                            которое будет отображаться при наведении, но этот пункт не является обязательным.
                        </Typography>
                        <Typography sx={{ mt: 2 }}>
                            Пример.
                            <br/>
                            <div className='example'>
                                [пример](http://example.com/ "Необязательная подсказка")<br/>

                            </div>
                            <br/>
                            Результат в markdown
                            <br/>
                            {Markdown("[пример](http://example.com/ \"Необязательная подсказка\")")}
                        </Typography>
                        <br/>
                        <Typography id="modal-modal-title" variant="h6" component="h2">
                            Изображения
                        </Typography>
                        <Typography  id="modal-modal-description" sx={{ mt: 2 }}>
                            Для того чтобы вставить изображение в Markdown нужно поместить альтернативный текст в квадратные скобки
                            и перед квадратными скобками поставить знак восклицания.
                            Затем круглые скобки, содержащие URL-адрес или относительный путь изображения,
                            а также (необязательно) всплывающую подсказку, заключённые в двойные или одиночные кавычки.
                        </Typography>
                        <Typography sx={{ mt: 2 }}>
                            Пример.
                            <br/>
                            <div className='example'>
                                ![Альтернативный текст](https://www.vectorlogo.zone/logos/reactjs/reactjs-ar21.svg "Подсказка")<br/>
                            </div>
                            <br/>
                            Результат в markdown
                            <br/>
                            {Markdown("![Альтернативный текст](https://www.vectorlogo.zone/logos/reactjs/reactjs-ar21.svg \"Подсказка\")")}
                        </Typography>
                        <br/>
                        <Typography  id="modal-modal-description" sx={{ mt: 2 }}>
                           Если вы хотите узнать больше, то прочитайте документацию <a href='https://akawah.ru/linux/markdown.html'>markdown</a>
                        </Typography>
                    </Box>
                </Box>
            </Modal>
        </div>
    );
}