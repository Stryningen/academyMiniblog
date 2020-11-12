let delete_article_button_node = document.getElementById("delete-article");
let delete_comment_node_button;
let is_showing = false;
let main_node = document.querySelector("main");
let comment_node_list = document.querySelectorAll(".comment-edit-item")

let pop_up_node = document.createElement("div");
pop_up_node.classList.add("pop-up-window");

pop_up_node.innerHTML = `
    <p>Are you sure?</p>
    <p>Once it is deleted it is lost forever.</p>
    <div id="pop-up-action">
	<input id="back-button" type="button" class="button" value="Back">
	<input type="submit" class="button" value="Delete">
    </div>
`;
function popup(){
    is_showing = !is_showing;
    if(is_showing){
	form_node.appendChild(pop_up_node);
	let pop_up_back_node = document.getElementById("back-button");
	pop_up_back_node.addEventListener("click", popup);
    } else {
	pop_up_node.remove();
    }
}

if(delete_article_button_node != null){
delete_article_button_node.addEventListener("click", ()=>{
    form_node = document.getElementById("delete-article-form");
    popup();
});
}

if(comment_node_list.length > 0){
    for(let i = 0; i < comment_node_list.length; i++){
	comment_node_list[i].getElementsByClassName("delete-comment-button")[0].addEventListener("click", ()=>{
    form_node = comment_node_list[i].lastElementChild;
    popup();
});
    }
}

