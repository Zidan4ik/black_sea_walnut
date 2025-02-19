function initSwipers(class_) {
    document.querySelectorAll(`.${class_}`).forEach(container => {
        new Swiper(container, {
            slidesPerView: 1,
            spaceBetween: 10,
            zoom: true,
            navigation: {
                nextEl: container.querySelector('.swiper-button-next'),
                prevEl: container.querySelector('.swiper-button-prev'),
            },
        });
    });
}