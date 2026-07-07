console.log("boardRegister.js in");

document.getElementById('trigger').addEventListener('click',()=>{
    document.getElementById('file').click();
});

const regExp = new RegExp("\.(exe|sh|bat|dll|jar|msi)$");
const maxSize = 1024*1024*10; // 10MB

// 실행파일 막기, 10MB 이상 사이즈 제한
function fileVaild(fileName, fileSize){
    if(regExp.test(fileName)){
        return 0;
    } else if(fileSize > maxSize){
        return 0;
    }else{
        return 1;
    }
}

document.getElementById('file').addEventListener('change', (e)=>{
    const fileObject = e.target.files;
    console.log(fileObject);   // FileList[]

    const div = document.getElementById('fileZone');
});

/*
        <ul class="list-group list-group-flush">
            <li class="list-group-item">
                <div class="mb-3">
                    <div class="fw-bold mb-2">업로드 가능 여부</div>
                    fileName
                    <span class="badge rounded-pill text-bg-primary">fileSize</span>
                </div>
            </li>
        </ul>
* */