document.addEventListener('DOMContentLoaded', () => {
    // 验证码点击刷新
    const captchaImg = document.querySelector('#captchaImg');
    if (captchaImg) {
        captchaImg.addEventListener('click', () => {
            captchaImg.src = 'captcha?' + Math.random();
        });
    }

    // 回复表单验证
    const replyForm = document.querySelector('#replyForm');
    if(replyForm) {
        replyForm.addEventListener('submit', e => {
            const content = replyForm.querySelector('textarea[name="content"]').value.trim();
            if(!content) {
                e.preventDefault();
                alert('回复不能为空');
            }
        });
    }

    // 发帖表单验证
    const postForm = document.querySelector('form[action="messages"]');
    if(postForm) {
        postForm.addEventListener('submit', e => {
            const title = postForm.querySelector('input[name="title"]').value.trim();
            const content = postForm.querySelector('textarea[name="content"]').value.trim();
            if(!title || !content) {
                e.preventDefault();
                alert('标题和内容不能为空');
            }
        });
    }

    // 自动刷新逻辑
    const REFRESH_INTERVAL = 5000;
    const currentPageUrl = window.location.href;
    let intervalId;

    function startAutoRefresh() {
        intervalId = setInterval(() => {
            const isTyping = document.activeElement.tagName === 'TEXTAREA' ||
                document.activeElement.tagName === 'INPUT';
            if (!isTyping) {
                window.location.reload();
            } else {
                console.log("用户正在输入，暂停自动刷新。");
            }
        }, REFRESH_INTERVAL);
    }

    function stopAutoRefresh() {
        clearInterval(intervalId);
    }

    if (currentPageUrl.includes("messages")) {
        startAutoRefresh();

        window.addEventListener('blur', stopAutoRefresh);
        window.addEventListener('focus', startAutoRefresh);
    }
});
