require: slotfilling/slotFilling.sc
  module = sys.zb-common
theme: /

    state: Start
        q!: $regex</start>
        a: Здравствуйте, я бот-помощник Ренессанс Банка, чем я могу вам помочь?

    state: Hello
        intent!: /привет
        a: Здравствуйте! Чем я могу вам помочь?

    state: Bye
        intent!: /пока
        a: До свидания!
        script: $jsapi.stopSession();

    state: NoMatch
        event!: noMatch
        a: Я не понял. Вы сказали: {{$request.query}}
        
    state: Hotline
        intent!: /Горячая линия
        script:
            $session.hotlineAttempts = $session.hotlineAttempts || 0;
            $session.hotlineAttempts++;
        if: $session.hotlineAttempts === 1
            a: Конечно, телефон службы поддержки есть. Но, возможно, я смогу помочь вам быстрее, так как многие вопросы можно решить прямо в чате. Что именно вас интересует?
        else:
            a: Я могу перевести вас на оператора для решения вопроса в чате, устроит?
            buttons:
                { text: "Да", transition: "/Hotline/Transfer"}
                { text: "Нет", transition: "/Hotline/Number"}
        state: Transfer
            a: Перевожу...
        
        state: Number
            a: Номер поддержки 8 (495) 981-0-981 работает 24/7. Звонок платный, стоимость зависит от тарифов вашего оператора связи.
            go!: /CloseDialog
        
    state: Payment
        intent!: /Способы оплаты
        a: Какой тип оплаты вас интересует?
        buttons:
            { text: "Как пополнить в приложении?", transition: "/Payment/App"}
            { text: "Где внести наличные?", transition: "/Payment/Cash"}
            { text: "Сложности с оплатой", transition: "/Payment/Problems"}
            
        state: App
            a: Для пополнения продукта перейдите в него и выберите «Пополнить». 💰 ознакомиться с комиссией можно при оплате.
            go!: /CloseDialog
        state: Cash
            a: Внести наличные можно:  
                • в офисе нашего банка (банкомат/терминал/касса);  
                • в банкоматах: «ВТБ», «Альфа-Банка», «Райффайзенбанк».
                Комиссии нет, а внести можно от 500тыс. до 1.5 млн. \n\n
                Подобрать удобный адрес и ознакомиться с режимом работы можно в разделе [«Отделения и банкоматы»](https://rencredit.ru/addresses/) \n\n
                🏛 Подробная информация о всех способах оплаты доступна на нашем сайте в разделе [«Платежи и переводы»](https://rencredit.ru/payment/).
            go!: /CloseDialog        
        state: Problems
            a: Вас понял, уже перевожу
            go!: /CloseDialog
        
    state: CloseDialog
        a: Остались ли у вас ещё вопросы? Могу ещё рассказать про:
        buttons:
            {text: "💳 Оплата", transition: "/Payment"}
            {text: "📞 Поддержка", transition: "/Hotline"}
            {text: "❌ Нет вопросов", transition: "/Bye"}
            