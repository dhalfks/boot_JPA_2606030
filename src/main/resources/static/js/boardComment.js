console.log("boardComment.js in");

// cmtAddBtn 버튼을 클릭하면 입력한 댓글과 작성자, bno 값을 controller 전송
document.getElementById('cmtAddBtn').addEventListener('click',()=>{
    const cmtText = document.getElementById('cmtText');
    const cmtWrtier = document.getElementById('cmtWriter');

    if(cmtText == null || cmtText.value.trim() == ''){
        alert('댓글 입력요망!');
        cmtText.focus();
        return false;
    }
    let cmtData = {
        bno: bno,
        writer: cmtWrtier.innerText,
        content: cmtText.value
    }
    console.log(cmtData);
    registerCommentToServer(cmtData).then(result => {
        if(result == '1'){
            alert("댓글 등록 성공!");
            // 댓글 입력창을 비우고, 포커스 맞추기
            cmtText.value='';
            cmtText.focus();
        }else{
            alert("댓글 등록 실패!");
            cmtText.focus();
        }
        // 댓글 리스트 호출
        spreadCommentList(bno);
    });
});

// list를 화면에 출력하는 함수
function spreadCommentList(bno) {
    commentListFromServer(bno).then(result =>{
        console.log(result);
        const ul = document.getElementById('cmtListArea');
        if(result.length > 0){
            // 댓글이 있는 경우
            let li = '';
            for(let comment of result){
                li+=`<li class="list-group-item shadow-sm rounded-2" data-cno="${comment.cno}">`;
                li+=`<div class="ms-2 me-auto"> no. ${comment.cno}`;
                li+=`<div class="fw-bold">${comment.writer}</div>`;
                li+=`${comment.content}`;
                li+=`</div>`;
                li+=`<div class="d-flex justify-content-end gap-2">`;
                li+=`<span class="badge rounded-pill text-bg-primary">${comment.regDate}</span>`;
                li+=`<button type="button" class="btn btn-outline-warning btn-sm mod">%</button>`;
                li+=`<button type="button" class="btn btn-outline-danger btn-sm del">X</button>`;
                li+=`</div>`;
                li+=`</li>`;
            }
            ul.innerHTML = li;
        }else{
            // 댓글이 없는 경우
            ul.innerHTML = `<li class="list-group-item shadow-sm rounded-2">등록된 댓글이 없습니다.</li>`;
        }
    });
}

document.getElementById('cmtListArea').addEventListener('click',(e)=>{
    if(e.target.classList.contains("del")){
        // 삭제버튼 인지
        // cno 값 추출 => closest (내가 속한 내 부모 값 찾기)
        let li = e.target.closest('li');
        let cno = li.dataset.cno;
        commentRemoveToServer(cno).then(result =>{
            if(result == "1"){
                alert("삭제성공!!");
                spreadCommentList(bno);
            }
        })
    }
})



// 전송 async 데이터 보내기
async function registerCommentToServer(cmtData){
    try {
        const url = "/comment/post";
        const config = {
            method:'post',
            headers : {
                'content-type': 'application/json; charset=utf-8'
            },
            body: JSON.stringify(cmtData)
        };

        const response = await  fetch(url, config);
        const result = await response.text();
        return result;
    }catch (e) {
        console.log(e);
    }
}

// list
async function commentListFromServer(bno){
    try{
        const response = await fetch("/comment/list/"+bno);
        const result = await response.json();
        return result;
    }catch (e) {
        console.log(e);
    }
}

// remove
async function commentRemoveToServer(cno){
    try{
        // fetch(url, config)
        const response = await fetch("/comment/remove/"+cno,
            {method:"delete"});
        const result = await response.text();
        return result;
    }catch (e) {
        console.log(e);
    }
}