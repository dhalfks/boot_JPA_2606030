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
    });
    // 댓글 리스트 호출
    commentListFromServer(bno).then(result =>{
        console.log(result);
    });

})


/* <ul class="list-group list-group-flush" id="cmtListArea">
            <li class="list-group-item shadow-sm rounded-2">
                <div class="ms-2 me-auto">
                    <div class="fw-bold">writer</div>
                    content가 많아지면 글자는 어떻게 될까요???
                </div>
                <span class="badge rounded-pill text-bg-primary">regDate</span>
                <div class="d-flex justify-content-end gap-2">
                    <button type="button" class="btn btn-outline-warning btn-sm">%</button>
                    <button type="button" class="btn btn-outline-danger btn-sm">X</button>
                </div>
            </li>
        </ul>
* */


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