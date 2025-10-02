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
window.addEventListener('load', () => {
  const scrapIcons = document.querySelectorAll('.scrap-icon');

  scrapIcons.forEach(icon => {
    icon.addEventListener('click', () => {
      const currentSrc = icon.getAttribute('src');
      // toggle: 채워진 이미지 ↔ 빈 이미지
      if (currentSrc.includes('scrap_full')) {
        icon.setAttribute('src', '/img/scrap.png');
      } else {
        icon.setAttribute('src', '/img/scrap_full.png');
      }
    });
  });
});

