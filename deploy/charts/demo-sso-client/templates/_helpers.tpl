{{/* vim: set filetype=mustache: */}}
{{/*
Expand the name of the chart.
*/}}
{{- define "demo-sso-client.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "demo-sso-client.fullname" -}}
{{- if .Values.fullnameOverride -}}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- $name := default .Chart.Name .Values.nameOverride -}}
{{- if contains $name .Release.Name -}}
{{- .Release.Name | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" -}}
{{- end -}}
{{- end -}}
{{- end -}}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "demo-sso-client.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Common labels
*/}}
{{- define "demo-sso-client.labels" -}}
helm.sh/chart: {{ include "demo-sso-client.chart" . }}
{{ include "demo-sso-client.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end -}}

{{/*
Selector labels
*/}}
{{- define "demo-sso-client.selectorLabels" -}}
app.kubernetes.io/name: {{ include "demo-sso-client.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end -}}

{{/*
Create the name of the service account to use
*/}}
{{- define "demo-sso-client.serviceAccountName" -}}
{{- if .Values.serviceAccount.create -}}
    {{ default (include "demo-sso-client.fullname" .) .Values.serviceAccount.name }}
{{- else -}}
    {{ default "default" .Values.serviceAccount.name }}
{{- end -}}
{{- end -}}

{{/*
Image Name
*/}}
{{- define "demo-sso-client.imageName" -}}
{{- if .Values.image.ghcr.enabled -}}
{{ .Values.image.ghcr.name }}
{{- else if .Values.image.aliyuncr.enabled -}}
{{ .Values.image.aliyuncr.name }}
{{- else -}}
{{ .Values.image.default.name }}
{{- end -}}
{{- end -}}

{{/*
Image Pull Secret
*/}}
{{- define "demo-sso-client.imagePullSecret" -}}
{{- if .Values.image.ghcr.enabled -}}
{{ .Values.image.ghcr.pullSecret }}
{{- else if .Values.image.aliyuncr.enabled -}}
{{ .Values.image.aliyuncr.pullSecret }}
{{- else -}}
{{ .Values.image.default.pullSecret }}
{{- end -}}
{{- end -}}

{{/*
Image Pull Policy
*/}}
{{- define "demo-sso-client.imagePullPolicy" -}}
{{- if eq .Values.image.tag "master" -}}
Always
{{- else -}}
IfNotPresent
{{- end -}}
{{- end -}}