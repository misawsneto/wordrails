{% set title = 'Dashboard' %}
{% set aside_class = 'folded' %}
{% extends "blocks/layout.html" %}

{% block page_content %}

{% include '../app/views/pages/dashboard.html' %}

{% endblock %}
