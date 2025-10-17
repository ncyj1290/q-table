window.addEventListener('load', () => {
	const viewport = document.querySelector('.recommend-section');
	const track = document.querySelector('.recommend-cards');
	const VISIBLE_COUNT = 5;

	const SPEED = 35; // 카드 움직임 속도

	if (!viewport || !track) return;

	const firstCard = track.querySelector('.card');
	const cardWidth = firstCard.offsetWidth;
	const gap = parseFloat(getComputedStyle(track).gap) || 0;

	const viewportWidth = VISIBLE_COUNT * cardWidth + (VISIBLE_COUNT - 1) * gap;
	viewport.style.width = viewportWidth + 'px';

	track.innerHTML = track.innerHTML + track.innerHTML;

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

//스크립 기능
//window.addEventListener('load', () => {
//	const scrapIcons = document.querySelectorAll('.scrap-icon');
//
//	scrapIcons.forEach(icon => {
//		icon.addEventListener('click', () => {
//			const storeIdx = icon.dataset.storeIdx;
//
//			const currentSrc = icon.getAttribute('src');
//			const isScrapped = currentSrc.includes('scrap_full');
//			icon.setAttribute('src', isScrapped ? '/img/scrap.png' : '/img/scrap_full.png');
//
//			// 서버 전송
//			fetch(`/scrap/toggle?storeIdx=${storeIdx}`, { method: 'POST' })
//				.then(res => res.json())
//				.then(data => console.log(data))
//				.catch(err => console.error(err));
//		});
//	});
//});
window.addEventListener('load', () => {
    const scrapIcons = document.querySelectorAll('.scrap-icon');

    // CSRF 토큰 가져오기
    const csrfToken = document.querySelector('meta[name="_csrf"]').content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

    scrapIcons.forEach(icon => {
        icon.addEventListener('click', () => {
            const storeIdx = icon.dataset.storeIdx;

            const currentSrc = icon.getAttribute('src');
            const isScrapped = currentSrc.includes('scrap_full');
            icon.setAttribute('src', isScrapped ? '/img/scrap.png' : '/img/scrap_full.png');

            // 서버 전송
            fetch(`/scrap/toggle`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    [csrfHeader]: csrfToken // CSRF 토큰 헤더 추가
                },
                body: `storeIdx=${storeIdx}` // POST body로 전달
            })
            .then(res => res.json())
            .then(data => console.log(data))
            .catch(err => console.error(err));
        });
    });
});

