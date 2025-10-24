window.addEventListener('load', () => {
  const viewport = document.querySelector('.recommend-section');
  const track = document.querySelector('.recommend-cards');
  const VISIBLE_COUNT = 5;
  const SPEED = 35;

  if (!viewport || !track) return;

  const firstCard = track.querySelector('.card');
  if (!firstCard) {
    console.warn('추천 카드 요소를 찾을 수 없음');
    return;
  }
  const cardWidth = firstCard.offsetWidth;
  const gap = parseFloat(getComputedStyle(track).gap) || 0;
  const viewportWidth = VISIBLE_COUNT * cardWidth + (VISIBLE_COUNT -1) * gap;
  
  viewport.style.width = viewportWidth + 'px';
  track.innerHTML += track.innerHTML;

  let last = performance.now();

  function loop(now) {
    const dt = now - last;
    last = now;
    const dist = SPEED * dt / 1000;
    track.scrollLeft += dist;

    const half = track.scrollWidth / 2;
    if (track.scrollLeft >= half) {
      track.scrollLeft -= half;
    }
    requestAnimationFrame(loop);
  }

  requestAnimationFrame(loop);
});



// 스크랩
window.addEventListener('load', () => {
    const scrapIcons = document.querySelectorAll('.scrap-icon');
    const csrfToken = document.querySelector('meta[name="_csrf"]').content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

    // 페이지 로드시 저장된 상태 복원
    scrapIcons.forEach(icon => {
        const storeIdx = icon.dataset.storeIdx;
        const storedState = localStorage.getItem(`scrap_${storeIdx}`);
        if (storedState === 'true') {
            icon.setAttribute('src', '/img/scrap_full.png');
        } else if (storedState === 'false') {
            icon.setAttribute('src', '/img/scrap.png');
        }
    });	

    scrapIcons.forEach(icon => {
        icon.addEventListener('click', () => {
            const storeIdx = icon.dataset.storeIdx;
            const currentSrc = icon.getAttribute('src');
            const isScrapped = currentSrc.includes('scrap_full');

            // 서버에 토글 요청
            fetch('/scrap/toggle', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    [csrfHeader]: csrfToken
                },
                body: `storeIdx=${storeIdx}`
            })
            .then(res => res.json())
            .then(data => {
                if (data.status === 'success') {
                    const newSrc = data.isScrapped ? '/img/scrap_full.png' : '/img/scrap.png';
                    icon.setAttribute('src', newSrc);

                    // 상태를 localStorage에 저장
                    localStorage.setItem(`scrap_${storeIdx}`, data.isScrapped);
                } else {
                    console.error("스크랩 토글 실패");
                }
            })
            .catch(err => console.error(err));
        });
    });
});



