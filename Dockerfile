
# Stage that builds the application, a prerequisite for the running stage
FROM maven:3.8.1-openjdk-17-slim as build
RUN curl -sL https://deb.nodesource.com/setup_18.x | bash -
RUN apt-get update -qq && apt-get install -qq --no-install-recommends nodejs

# Stop running as root at this point
RUN useradd -m myuser
WORKDIR /usr/src/app/
RUN chown myuser:myuser /usr/src/app/
USER myuser

# Copy pom.xml and prefetch dependencies so a repeated build can continue from the next step with existing dependencies
COPY --chown=myuser pom.xml ./
RUN mvn dependency:go-offline -Pproduction -Dvaadin.offlineKey='eyJraWQiOiI1NDI3NjRlNzAwMDkwOGU2NWRjM2ZjMWRhYmY0ZTJjZDI4OTY2NzU4IiwidHlwIjoiSldUIiwiYWxnIjoiRVM1MTIifQ.eyJhbGxvd2VkUHJvZHVjdHMiOlsidmFhZGluLWNvbGxhYm9yYXRpb24tZW5naW5lIiwidmFhZGluLXRlc3RiZW5jaCIsInZhYWRpbi1kZXNpZ25lciIsInZhYWRpbi1jaGFydCIsInZhYWRpbi1ib2FyZCIsInZhYWRpbi1jb25maXJtLWRpYWxvZyIsInZhYWRpbi1jb29raWUtY29uc2VudCIsInZhYWRpbi1yaWNoLXRleHQtZWRpdG9yIiwidmFhZGluLWdyaWQtcHJvIiwidmFhZGluLW1hcCIsInZhYWRpbi1zcHJlYWRzaGVldC1mbG93IiwidmFhZGluLWNydWQiLCJ2YWFkaW4tY2xhc3NpYy1jb21wb25lbnRzIiwidmFhZGluLXBvcnRsZXQiLCJ2YWFkaW4tb3NnaSIsInZhYWRpbi1kc3B1Ymxpc2hlciIsImZsb3ctcG9seW1lci10ZW1wbGF0ZSJdLCJzdWIiOiJmYWQxYmExYS0zYWUyLTQxMzYtYjczNS1mMGViNTcxNThjM2EiLCJ2ZXIiOjEsImlzcyI6IlZhYWRpbiIsIm5hbWUiOiJNYXJ0aW4gU2VwcGkiLCJhbGxvd2VkRmVhdHVyZXMiOlsiY2VydGlmaWNhdGlvbnMiLCJzcHJlYWRzaGVldCIsInRlc3RiZW5jaCIsImRlc2lnbmVyIiwiY2hhcnRzIiwiYm9hcmQiLCJhcHBzdGFydGVyIiwicHJpbWUtcHJvZHVjdHMtMjAyMjEwIiwidmlkZW90cmFpbmluZyIsInByby1wcm9kdWN0cy0yMDIyMTAiXSwibWFjaGluZV9pZCI6bnVsbCwic3Vic2NyaXB0aW9uIjoiVmFhZGluIFBybyBMZWdhY3kgVHJpYWwiLCJidWlsZF90eXBlcyI6WyJwcm9kdWN0aW9uIl0sImV4cCI6MTcxODkyODAwMCwiaWF0IjoxNjg3NTQ3MzU1LCJhY2NvdW50IjoiTWFuYWdlbWVudCBDZW50ZXIgSW5uc2JydWNrIn0.AXIVs-CsaLDzSRK6R1n8MkNtYRgvEIrAk-G6N5-VmGmnDbwsTScJpn6Tk-jpA9r-o86DWTJwlr7Uk2vuJZEfbPYWAI7Yv70SatwhUFqdulMWUlcBRpGLfGyxdPjW_PC4FOWjl-Ko-6nzyTM7Nk40Xpa6wnsK84hYCzwWolhfXXQaAPPy'

# Copy all needed project files to a folder
COPY --chown=myuser:myuser src src
COPY --chown=myuser:myuser frontend frontend

# Build the production package, assuming that we validated the version before so no need for running tests again.
RUN mvn clean package -DskipTests -Pproduction

# Running stage: the part that is used for running the application
FROM maven:3.8.1-openjdk-17-slim
RUN curl -sL https://deb.nodesource.com/setup_18.x | bash -
RUN apt-get update -qq && apt-get install -qq --no-install-recommends nodejs

COPY --from=build /usr/src/app/target/*.jar /usr/app/app.jar
RUN useradd -m myuser
USER myuser
EXPOSE 5432
CMD java -jar /usr/app/app.jar
