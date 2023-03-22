from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager

PATH = "E:\Program Files\chromedriver_win32\chromedriver.exe"

def launchBrowser(url):
  chrome_options = webdriver.ChromeOptions()
  chrome_options.add_argument('--allow-insecure-localhost')
  chrome_options.add_experimental_option("detach", True)
  driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()), options=chrome_options)
  driver.get(url)
  print(url)
  return driver

def main():
  driver = launchBrowser("http://localhost:3000")

  


if __name__=="__main__":
  main()