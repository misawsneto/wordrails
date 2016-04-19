{% set title = 'Wall' %}
{% set aside_class = 'folded' %}

{% extends "blocks/layout.html" %}

{% block page_content %}

{% include '../app/views/pages/dashboard.wall.html' %}

{% endblock %}
