async function changeLanguage(key, lang) {
    const response = await fetch(`/locale/message?key=${key}&lang=${lang}`);
    return await response.text();
}