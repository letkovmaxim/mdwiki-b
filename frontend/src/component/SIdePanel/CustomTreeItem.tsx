import * as React from 'react';
import TreeItem, {
    TreeItemProps,
    useTreeItem,
    TreeItemContentProps,
} from '@mui/lab/TreeItem';
import clsx from 'clsx';
import Typography from '@mui/material/Typography';
import {useDispatch, useSelector} from "react-redux";
import {treePages} from "../../redux/actions";

const CustomContent = React.forwardRef(function CustomContent(
    props: TreeItemContentProps,
    ref,
) {
    const {
        classes,
        className,
        label,
        nodeId,
        icon: iconProp,
        expansionIcon,
        displayIcon,
    } = props;

    const {
        disabled,
        expanded,
        selected,
        focused,
        handleExpansion,
        handleSelection,
        preventSelection,
    } = useTreeItem(nodeId);

    const icon = iconProp || expansionIcon || displayIcon;

    const dispatch = useDispatch()
    const treeP = useSelector((state:any) => state.app.tree)

    const handleMouseDown = (event: React.MouseEvent<HTMLDivElement, MouseEvent>) => {
        preventSelection(event);
    };

    const handleExpansionClick = (
        event: React.MouseEvent<HTMLDivElement, MouseEvent>,
    ) => {
        handleExpansion(event);
        let arr: string[] = treeP
        if(treeP.includes(nodeId)){
            arr = arr.filter((id) => id !== nodeId);
        }else {
            arr.push(nodeId)
        }
        dispatch(treePages(arr))
    };

    const handleSelectionClick = (
        event: React.MouseEvent<HTMLDivElement, MouseEvent>,
    ) => {
        handleSelection(event);
    };

    return (
        <div
            className={clsx(className, classes.root, {
                [classes.expanded]: expanded,
                [classes.selected]: selected,
                [classes.focused]: focused,
                [classes.disabled]: disabled,
            })}
            onMouseDown={handleMouseDown}
            ref={ref as React.Ref<HTMLDivElement>}
        >
            <div onClick={handleExpansionClick} className={classes.iconContainer}>
                {icon}
            </div>
            <Typography
                onClick={handleSelectionClick}
                component="div"
                className={classes.label}
            >
                {label}
            </Typography>
        </div>
    );
});

function CustomTreeItem(props: TreeItemProps) {
    return <TreeItem ContentComponent={CustomContent} {...props} />;
}

export default CustomTreeItem;